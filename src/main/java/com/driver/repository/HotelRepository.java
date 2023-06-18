package com.driver.repository;

import com.driver.model.Booking;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class HotelRepository {

    public HashMap<String, Hotel> hotelDb = new HashMap<>();
    public HashMap<Integer, User> userDb = new HashMap<>();
    public HashMap<String, Booking> bookingDb = new HashMap<>();
}
