package com.becoder.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesDto.CategoryDto;
import com.becoder.entity.Notes;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.CategoryRepository;
import com.becoder.repository.NotesRepository;

import NotesService.NoteService;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private NotesRepository notesRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Boolean saveNotes(NotesDto notesDto) throws Exception {

		checkCategoryExist(notesDto.getCategory());

		Notes notes = mapper.map(notesDto, Notes.class);
		Notes saveNotes = notesRepository.save(notes);

		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}

		return false;
	}

	private void checkCategoryExist(CategoryDto category) throws ResourceNotFoundException {
		categoryRepository.findById(category.getId())
				.orElseThrow(() -> new ResourceNotFoundException("category id invalid"));

	}

	@Override
	public List<NotesDto> getAllNotes() {
		return notesRepository.findAll().stream().map(note -> mapper.map(note, NotesDto.class)).toList();
	}

}
