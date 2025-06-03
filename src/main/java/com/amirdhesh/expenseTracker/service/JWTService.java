package com.amirdhesh.expenseTracker.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JWTService {
    private String secretKey;

    public JWTService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256"); //Keygenerator creates random key Compatable for "HmacSHA256" algorithm this Ensures that key can't be used for other algorithms
            SecretKey sk = keyGen.generateKey();//A 256-bit (32-byte) byte array is generated randomly
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
            //Problem: Bytes may contain non-printable/control characters (e.g., 0x00, 0xFF).
            //Solution: Base64 encodes them into ASCII characters (A-Z, a-z, 0-9, +, /, =).
            //Base64 is a binary-to-text encoding scheme that converts raw binary data (like cryptographic keys, images, or files) into a printable ASCII string format. It uses 64 characters to represent binary data safely in text-based systems (e.g., JSON, HTTP, emails).
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public String generateToken(String username) {
        //The claims map is used to store custom data (key-value pairs) inside a JWT (JSON Web Token). These claims provide additional context about the user or token itself, beyond the standard fields like subject or expiration.
        HashMap<String, Object> claims = new HashMap<>();
        // Claim Customisation Example:
        //    claims.put("role", role);       Custom claim: user role
        //    claims.put("email", email);     Custom claim: user email
        //    claims.put("isActive", true);   Custom claim: account status
        return Jwts.builder()
                .claims() // Start claims section
                .add(claims) // Inject custom claims (empty here, but extensible)
                .subject(username) // Standard claim: who the token represents
                .issuedAt(new Date(System.currentTimeMillis())) // Standard claim: token creation time
                .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // Standard claim: token expiry (30 hours later)
                .and() // Close claims section
                .signWith(getKey()) // Sign the token
                .compact(); // Generate the final token string
        //{
        //  "sub": "username123",
        //  "iat": 1718000000,
        //  "exp": 1718086400,
        //  "role": "ADMIN", (will not be present in this code context)
        //  "email": "user@example.com", (will not be present in this code Context)
        //  "isActive": true (will not be present in this code context)
        //} this example is for if we have custom claim
    }

    public SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); //Decoding back into binary since JWT only takes binary
        return Keys.hmacShaKeyFor(keyBytes); //converts a byte array into a SecretKey for HMAC-SHA algorithms (Makes a Key Compatible with HMAC-SHA256)
    }

    //Purpose: Extracts the username (JWT subject) from the token.
    //Process: Delegates to extractClaim() using Claims::getSubject to fetch the subject claim.
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //claimResolver: Functional interface (e.g., Claims::getSubject, Claims::getExpiration) specifying which claim to extract
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims); // applies getSubject() to JWT claim object which contain {subject:xxx.expiration:xxxx,...}
    }

    //Configures the JWT parser with the secret key via .verifyWith(getKey()).
    //parseSignedClaims(token) verifies the token's signature and parses it.
    //getPayload() returns the claims (e.g., subject, expiration).
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey()) // Set verification key
                .build()
                .parseSignedClaims(token)// Parse and verify signature
                .getPayload();// Return claims body
    }

    //Purpose: Validates if the token is:
    //Issued for the correct user (matches UserDetails username).
    //Not expired.
    //Returns: true only if both conditions pass.
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //Extracts the expiration timestamp using extractExpiration().
    //Compares it against the current time (new Date()).
    //Returns true if expired.
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //Fetches the expiration claim from the token. Uses extractClaim() with Claims::getExpiration.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


}
