package com.llk.glide_fix_bug.glide_2;

public class GlideRetargetException extends Exception{

    private String redirectedUrl;

    public GlideRetargetException(String url){
        this.redirectedUrl = url;
    }

    public String getRedirectedUrlUrl(){
        return redirectedUrl;
    }
}
