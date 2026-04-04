package Security.Filter;

import Security.Entity.security.UserData;
import Security.Repo.UserDataRepo;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class MyUserDetails implements UserDetailsService {

    private final UserDataRepo userDataRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData user = userDataRepo.findByUsername(username).orElseThrow(()->new RuntimeException("User not Found"));

        return User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().replace("ROLE_",""))
                        .build();


    }
}
