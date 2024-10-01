package com.ezb.ebsec.config;

import com.ezb.ebsec.model.Customer;
import com.ezb.ebsec.repository.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbsecUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public EbsecUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Details not found for the user: " + username));
        return new User(customer.getEmail(), customer.getPwd(), List.of(new SimpleGrantedAuthority(customer.getRole())));
    }

}
