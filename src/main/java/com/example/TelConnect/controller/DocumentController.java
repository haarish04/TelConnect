package com.example.TelConnect.controller;

import com.example.TelConnect.model.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.TelConnect.service.DocumentService;

import javax.print.Doc;
import java.util.List;

@RestController
@RequestMapping("/document")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService){
        this.documentService= documentService;
    }


    //Handler to create new document record
    @PostMapping("/save")
    public ResponseEntity<String> saveDocument(@RequestParam Long customerId, @RequestParam String DocumentType){
        documentService.saveDocument(customerId, DocumentType);
        return ResponseEntity.ok("Document entry successfull");
    }

    //Get the documents and their details for a customer
    @GetMapping("/get/{customerId}")
    public ResponseEntity<?> getDocument(@PathVariable Long customerId){
        List<Document> documents = documentService.getByCustomerId(customerId);
        if(documents!=null)
            return ResponseEntity.ok(documents);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User has not submitted any documents");
    }

}
