package fontys.emergencywebapps.persistence.repos;

import fontys.emergencywebapps.persistence.entities.VerificationTokenOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenOtpRepository extends JpaRepository<VerificationTokenOtp, Long> {
    VerificationTokenOtp findByOtp(String otp);

    VerificationTokenOtp findByUserEmail(String email);
}
