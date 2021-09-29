package com.cos.shumstart.service;

import com.cos.shumstart.domain.booth.Booth;
import com.cos.shumstart.domain.rental.Rental;
import com.cos.shumstart.domain.rental.RentalRepository;
import com.cos.shumstart.domain.umbrella.Umbrella;
import com.cos.shumstart.domain.user.User;
import com.cos.shumstart.domain.user.UserRepository;
import com.cos.shumstart.web.boothmodel.BoothRepository;
import com.cos.shumstart.web.boothmodel.UmbrellaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final BoothRepository boothRepository;
    private final UserRepository userRepository;
    private final UmbrellaRepository umbrellaRepository;

    @Transactional
    public Rental 대여하기(int userId, int umbrellaId, int boothId) {

        User userEntity = userRepository.findById(userId);

        Umbrella umbrella = new Umbrella();
        umbrella.setId(umbrellaId);

        Rental rental = new Rental();
        rental.setUser(userEntity);
        rental.setUmbrella(umbrella);

        Booth boothEntity = boothRepository.findById(boothId);
        int leftUmbrella = boothEntity.getLeftUmbrella();
        boothRepository.updateLeftUmbrella(--leftUmbrella, boothId);

        umbrellaRepository.updateBoothIdRental(umbrellaId);
        umbrellaRepository.updateRentalState(true, umbrellaId);

        userRepository.updateState(true, userId);

        return rentalRepository.save(rental);
    }
}
