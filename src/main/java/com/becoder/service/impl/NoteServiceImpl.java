package com.becoder.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesDto.CategoryDto;
import com.becoder.entity.FileDetails;
import com.becoder.entity.Notes;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.CategoryRepository;
import com.becoder.repository.FileRepository;
import com.becoder.repository.NotesRepository;
import com.becoder.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private NotesRepository notesRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper mapper;

	@Value("${file.upload.path}")
	private String uploadPath;
	@Autowired
	private FileRepository fileRepo;
	@Override
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception {

		ObjectMapper ob = new ObjectMapper();
		NotesDto notesDto = ob.readValue(notes, NotesDto.class);

		checkCategoryExist(notesDto.getCategory());
		Notes notesMap = mapper.map(notesDto, Notes.class);
		FileDetails fileDtls = saveFileDetails(file);

		if (!ObjectUtils.isEmpty(fileDtls)) {
			notesMap.setFileDetails(fileDtls);
		} else {
			notesMap.setFileDetails(null);
		}

		Notes saveNotes = notesRepository.save(notesMap);
		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}

		return false;
	}

	private FileDetails saveFileDetails(MultipartFile file) throws Exception {
		if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
			String originalFileName = file.getOriginalFilename();
			String extension = FilenameUtils.getExtension(originalFileName);

			List<String> extensionAllow = Arrays.asList("pdf", "xlsx", "jpg", "png");
			if (!extensionAllow.contains(extension)) {
				throw new IllegalArgumentException("Invalid file format ! Upload only .pdf,.xlsx,.jpg");
			}

			String rndString = UUID.randomUUID().toString();
			String uploadFileName = rndString + "." + extension;

			File saveFile = new File(uploadPath);
			if (!saveFile.exists()) {
				saveFile.mkdir();
			}
			String storePath = uploadPath.concat(uploadFileName);
			long upload = Files.copy(file.getInputStream(), Paths.get(storePath));

			if (upload != 0) {
				FileDetails fileDtls = new FileDetails();
				fileDtls.setOriginalFileName(originalFileName);
				fileDtls.setDisplayFileName(getDisplayName(originalFileName));
				fileDtls.setUploadFileName(uploadFileName);
				fileDtls.setFileSize(file.getSize());
				fileDtls.setPath(storePath);
				FileDetails saveFileDtls = fileRepo.save(fileDtls);
				return saveFileDtls;
			}
		}
		return null;
	}

	private String getDisplayName(String originalFileName) {
		String extension = FilenameUtils.getExtension(originalFileName);
		String fileName = FilenameUtils.removeExtension(originalFileName);

		if (fileName.length() > 8) {
			fileName = fileName.substring(0, 7);
		}
		fileName = fileName + "." + extension;
		return fileName;
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
