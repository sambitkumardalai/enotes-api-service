package com.becoder.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.becoder.dto.NotesDto;
import com.becoder.repository.CategoryRepository;
import com.becoder.repository.NotesRepository;

public interface NoteService {

	public Boolean saveNotes(NotesDto notesDto) throws Exception;

	public List<NotesDto> getAllNotes();

}
