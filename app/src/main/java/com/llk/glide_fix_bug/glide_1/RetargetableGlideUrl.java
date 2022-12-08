package com.llk.glide_fix_bug.glide_1;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.util.Preconditions;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Map;

/**
 * llk：源码拷贝自Glide4.1.1的GlideKey
 * 基于GlideKey源码进行修改，主要是增加了重定向方法
 */
public class RetargetableGlideUrl implements Key {

  private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%;$";

  private final Headers headers;

  @Nullable private final URL url;
  //llk：将stringUrl字符串去掉final，因为重定向的情况需要改变它的值
  @Nullable private volatile String stringUrl;

  @Nullable private String safeStringUrl;
  @Nullable private URL safeUrl;
  @Nullable private volatile byte[] cacheKeyBytes;

  private int hashCode;

  /**
   * llk：重定向stringUrl的值
   */
  public void redirectStringUrl(String stringUrl){
    this.stringUrl = stringUrl;

    //llk：stringUrl更新了，这些变量也需要重新计算。
    cacheKeyBytes = null;
    hashCode = 0;
  }

  public RetargetableGlideUrl(URL url) {
    this(url, Headers.DEFAULT);
  }

  public RetargetableGlideUrl(String url) {
    this(url, Headers.DEFAULT);
  }

  public RetargetableGlideUrl(URL url, Headers headers) {
    this.url = Preconditions.checkNotNull(url);
    this.stringUrl = null;
    this.headers = Preconditions.checkNotNull(headers);
  }

  public RetargetableGlideUrl(String url, Headers headers) {
    this.url = null;
    this.stringUrl = Preconditions.checkNotEmpty(url);
    this.headers = Preconditions.checkNotNull(headers);
  }

  public URL toURL() throws MalformedURLException {
    return getSafeUrl();
  }

  private URL getSafeUrl() throws MalformedURLException {
    if (safeUrl == null) {
      safeUrl = new URL(getSafeStringUrl());
    }
    return safeUrl;
  }

  public String toStringUrl() {
    return getSafeStringUrl();
  }

  private String getSafeStringUrl() {
    if (TextUtils.isEmpty(safeStringUrl)) {
      String unsafeStringUrl = stringUrl;
      if (TextUtils.isEmpty(unsafeStringUrl)) {
        unsafeStringUrl = Preconditions.checkNotNull(url).toString();
      }
      safeStringUrl = Uri.encode(unsafeStringUrl, ALLOWED_URI_CHARS);
    }
    return safeStringUrl;
  }

  public Map<String, String> getHeaders() {
    return headers.getHeaders();
  }

  public String getCacheKey() {
    return stringUrl != null ? stringUrl : Preconditions.checkNotNull(url).toString();
  }

  @Override
  public String toString() {
    return getCacheKey();
  }

  @Override
  public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    messageDigest.update(getCacheKeyBytes());
  }

  private byte[] getCacheKeyBytes() {
    if (cacheKeyBytes == null) {
      cacheKeyBytes = getCacheKey().getBytes(CHARSET);
    }
    return cacheKeyBytes;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof RetargetableGlideUrl) {
      RetargetableGlideUrl other = (RetargetableGlideUrl) o;
      return getCacheKey().equals(other.getCacheKey()) && headers.equals(other.headers);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (hashCode == 0) {
      hashCode = getCacheKey().hashCode();
      hashCode = 31 * hashCode + headers.hashCode();
    }
    return hashCode;
  }
}
