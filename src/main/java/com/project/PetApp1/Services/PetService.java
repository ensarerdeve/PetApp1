package com.project.PetApp1.Services;

import com.project.PetApp1.Models.Pet;
import com.project.PetApp1.Models.Post;
import com.project.PetApp1.Models.User;
import com.project.PetApp1.Repositories.PetRepository;
import com.project.PetApp1.Repositories.UserRepository;
import com.project.PetApp1.Requests.PetRequest;
import com.project.PetApp1.Responses.PetResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService {
    private PetRepository petRepository;
    private UserRepository userRepository;
    private PostService postService;
    public PetService(PetRepository petRepository, UserRepository userRepository, PostService postService) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.postService = postService;
    }
    public List<PetResponse> GetOneUsersPets(Long userId){
        List<Pet> pets = petRepository.findByUserId(userId);
        return pets.stream()
                .map(pet -> new PetResponse(pet.getId(), pet.getPetName(), pet.getUser().getId(),
                        pet.getPosts().stream().map(post -> post.getId()).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
    public PetResponse GetPetByPetId(Long id){
        Pet pet = petRepository.findPetById(id);
        if (pet != null){
            List<Long> postIds = pet.getPosts().stream()
                    .map(post -> post.getId())
                    .collect(Collectors.toList());
            return new PetResponse(pet.getId(), pet.getPetName(),pet.getUser().getId(),postIds);
        }
        else{
            throw new RuntimeException("Pet not found.");
        }
    }
    public Pet CreatePet(PetRequest petRequest){
        Optional<User> userOptional = userRepository.findById(petRequest.getUserId());
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Pet pet = new Pet();
            pet.setPetName(petRequest.getPetName());
            pet.setUser(user);
            return petRepository.save(pet);
        }
        else{
            throw new RuntimeException("User not found.");
        }
    }
    public Pet updatePet(Long id, PetRequest petRequest) {
        Optional<Pet> petOptional = petRepository.findById(id);
        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();
            pet.setPetName(petRequest.getPetName());

            Optional<User> userOptional = userRepository.findById(petRequest.getUserId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                pet.setUser(user);
            } else {
                throw new RuntimeException("User not found");
            }

            return petRepository.save(pet);
        } else {
            throw new RuntimeException("Pet not found");
        }
    }
    public void deletePet(Long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet != null) {
            postService.deletePostsByPetId(petId);
            petRepository.deleteById(petId);
        }
    }
}
