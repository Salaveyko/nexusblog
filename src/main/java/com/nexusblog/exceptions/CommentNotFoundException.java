package com.nexusblog.exceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(){
        super("Comment not found");
    }
}
