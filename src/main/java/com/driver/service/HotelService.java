package com.driver.service;


import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import com.driver.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HotelService {

    @Autowired
    HotelRepository hotelRepository;
    public String addHotel(Hotel hotel){

        //You need to add an hotel to the database
        //incase the hotelName is null or the hotel Object is null return an empty a FAILURE
        //Incase somebody is trying to add the duplicate hotelName return FAILURE
        //in all other cases return SUCCESS after successfully adding the hotel to the hotelDb.

        if(hotel == null || hotel.getHotelName()==null) return "FAILURE";
        if(hotelRepository.hotelDb.containsKey(hotel.getHotelName())) return "FAILURE";
          else
          {
              hotelRepository.hotelDb.put(hotel.getHotelName(),hotel);
              return "SUCCESS";
          }
    }


    public Integer addUser(User user){

        //You need to add a User Object to the database
        //Assume that user will always be a valid user and return the aadharCardNo of the user
        hotelRepository.userDb.put(user.getaadharCardNo(), user);
        return user.getaadharCardNo();
    }


    public String getHotelWithMostFacilities(){

        //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
        //Incase there is a tie return the lexicographically smaller hotelName
        //Incase there is not even a single hotel with atleast 1 facility return "" (empty string)
        int max = 0;
        String ans = "";
        for(Hotel hotel: hotelRepository.hotelDb.values())
        {
            if(hotel.getFacilities().size()>=max )
            {
                if(max == hotel.getFacilities().size() && hotel.getHotelName().compareTo(ans)<0) ans = hotel.getHotelName();
                else {
                    max = hotel.getFacilities().size();
                    ans = hotel.getHotelName();
                }
            }
        }

        return ans;
    }


    public int bookARoom(Booking booking){

        //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
        //Have bookingId as a random UUID generated String
        //save the booking Entity and keep the bookingId as a primary key
        //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
        //If there arent enough rooms available in the hotel that we are trying to book return -1
        //in other case return total amount paid

        String id = UUID.randomUUID().toString();
        Hotel hotel = hotelRepository.hotelDb.get(booking.getHotelName());
        if(booking.getNoOfRooms()>hotel.getAvailableRooms()) return -1;
        int fare = booking.getNoOfRooms()*hotel.getPricePerNight();
        booking.setBookingId(id);
        booking.setAmountToBePaid(fare);
        hotelRepository.bookingDb.put(id,booking);
        return fare;
    }


    public int getBookings(Integer aadharCard)
    {
        //In this function return the bookings done by a person
        int count = 0;
        for(Booking booking: hotelRepository.bookingDb.values())
        {
            if(booking.getBookingAadharCard()==aadharCard) count++;
        }
        return count;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName){

        //We are having a new facilites that a hotel is planning to bring.
        //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
        //return the final updated List of facilities and also update that in your hotelDb
        //Note that newFacilities can also have duplicate facilities possible
        Hotel hotel = hotelRepository.hotelDb.get(hotelName);
        List<Facility> oldFacilities = hotel.getFacilities();
        for(Facility facility : newFacilities)
        {
            if(!oldFacilities.add(facility)) oldFacilities.add(facility);
        }
        hotel.setFacilities(oldFacilities);
        hotelRepository.hotelDb.put(hotel.getHotelName(),hotel);
        return hotel;
    }
}
