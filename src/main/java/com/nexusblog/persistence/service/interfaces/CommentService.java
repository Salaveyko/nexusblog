package com.nexusblog.persistence.service.interfaces;

import com.nexusblog.dto.CommentDto;

public interface CommentService {
    CommentDto add(CommentDto commentDto) ;
    void delete(CommentDto commentDto);
}
