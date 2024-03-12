package fontys.emergencywebapps.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class VerificationTokenOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String otp;
    private Date expiryDate;
    private static final int EXPIRATION_TIME = 5;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public VerificationTokenOtp(String otp, User user) {
        super();
        this.otp = otp;
        this.user = user;
        this.expiryDate = this.getOTPExpirationDate();
    }
    public VerificationTokenOtp(String otp) {
        super();
        this.otp = otp;
        this.expiryDate = this.getOTPExpirationDate();
    }

    public Date getOTPExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
