package spring.io.security.springsecurityjwt.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import spring.io.security.springsecurityjwt.model.AuthenticationRequest;
import spring.io.security.springsecurityjwt.model.AuthenticationResponse;
import spring.io.security.springsecurityjwt.model.MyUserDetailsService;
import spring.io.security.springsecurityjwt.util.JwtUtil;

@RestController
public class HelloController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

//    @GetMapping(path = "/")
    @RequestMapping("/")
    public String sayHi(){
        return "Welcome";
    }

 //  @GetMapping(path = "/user")
    @RequestMapping("/user")
    public String user(){
        return "Welcome User";
    }

    @GetMapping(path = "/admin")
//    @RequestMapping("/admin")
    public String admin(){
        return "Welcome Admin";
    }


    @RequestMapping(value = "/authenticate",method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),authenticationRequest.getPassword()));
        }catch (BadCredentialsException e){
            throw new Exception("Incorrect username or password",e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
        final  String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
