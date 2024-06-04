package com.project.PetApp1.Controllers;

import com.project.PetApp1.Models.Pet;
import com.project.PetApp1.Models.User;
import com.project.PetApp1.Requests.PetRequest;
import com.project.PetApp1.Responses.PetResponse;
import com.project.PetApp1.Services.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {
    private PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }
    @GetMapping("/{id}")
    public PetResponse GetPetByPetId(@PathVariable Long id){
        return petService.GetPetByPetId(id);
    }
    @GetMapping("/byUser/{userId}")
    public List<PetResponse> GetPetsByUserId(@PathVariable Long userId){
        return petService.GetOneUsersPets(userId);
    }
    @PostMapping
    public ResponseEntity<Pet> CreatePet(@RequestBody PetRequest petRequest){
        Pet createdPet = petService.CreatePet(petRequest);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Pet> UpdatePet(@PathVariable Long id, @RequestBody PetRequest petRequest){
        Pet updatedUser = petService.updatePet(id, petRequest);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{petId}")
    public void DeletePet(@PathVariable Long petId){
        petService.deletePet(petId);
    }
}
