package com.nexusblog.persistence.service.interfaces;

import com.nexusblog.dto.CommentDto;

public interface CommentService {
    CommentDto add(CommentDto commentDto) ;
    void deleteById(Long id);
    CommentDto getById(Long commentId);
}
