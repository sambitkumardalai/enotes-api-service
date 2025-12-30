package com.becoder.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.entity.FileDetails;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.CategoryRepository;
import com.becoder.repository.NotesRepository;

public interface NoteService {

	public Boolean saveNotes(String notes, MultipartFile file) throws Exception;

	public List<NotesDto> getAllNotes() throws Exception;
	public FileDetails getFileDetails(Integer id) throws Exception;
	public byte[] downloadFile(FileDetails fileDtls) throws Exception;
	
	public NotesResponse getAllNotesByUser(Integer userId,Integer pageNo,Integer pageSize);

	public void softDeleteNotes(Integer id) throws Exception;

	public void restoreNotes(Integer id)throws Exception;

	public List<NotesDto> getUserRecycleBinNotes(Integer userId);

	public void hardDeleteNotes(Integer id)throws Exception;

	public void emptyRecycleBin(Integer userId);
}
