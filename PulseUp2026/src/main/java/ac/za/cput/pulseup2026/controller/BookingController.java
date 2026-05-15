package ac.za.cput.pulseup2026.controller;

import ac.za.cput.pulseup2026.domain.Appointment;
import ac.za.cput.pulseup2026.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService){
        this.bookingService = bookingService ;
    }
    @PostMapping
    public Appointment create(@RequestBody Appointment appointment){
        return bookingService.create(appointment);
    }
    @PutMapping
    public Appointment update(@RequestBody Appointment appointment ){
        return bookingService.update(appointment);
    }
    @GetMapping("/{id}")
    public Appointment read(@PathVariable Long id){
        return bookingService.read(id);
    }
    @GetMapping
    public List<Appointment> getAll() {
        return bookingService.getAll();
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        bookingService.delete(id);
    }
}
