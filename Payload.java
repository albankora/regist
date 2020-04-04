import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import java.nio.charset.StandardCharsets;

public class Payload {

    final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

     private String clientId;

     private String clientSecret;

     private String email;

     private String password;

     private String firstName;

     private String lastName;

     private String phoneNumber;

     private String birthdate; // the format is Y-m-d

     private String notifications; // this is eather "email" OR "both"

     private String gender; // this is eather "m" OR "f"

     private String termsAndConditions = "yes"; // termas need to be accepted by the user on the UI always

     private String brand = "zava";

     private String locale = "de_DE";

     public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public void setBirthdate(final String birthdate) {
        this.birthdate = birthdate;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public void setNotifications(final String notifications) {
        this.notifications = notifications;
    }

    public void setTermsAndConditions(final String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public void setBrand(final String brand) {
        this.brand = brand;
    }

    public void setLocale(final String locale) {
        this.locale = locale;
    }

    public String build(){

        String secretHash = this.calculateSecretHash(this.clientId, this.clientSecret, this.email);

        return
        "{"
        +" \"ClientId\": \"" + this.clientId + "\",                            "
        +" \"SecretHash\": \"" + secretHash + "\",                             "
        +" \"Username\": \"" + this.email + "\",                               "
        +" \"Password\": \"" + this.password+ "\",                             "
        +" \"UserAttributes\": [{                                              "
        +"      \"Name\": \"given_name\",                                      "
        +"      \"Value\": \"" + this.firstName + "\"                          "
        +"     }, {                                                            "
        +"       \"Name\": \"family_name\",                                    "
        +"       \"Value\": \"" + this.lastName + "\"                          "
        +"     }],                                                             "
        +" \"ClientMetadata\": {                                               "
        +"    \"birthdate\": \"" + this.birthdate + "\",                       " 
        +"     \"gender\": \"" + this.gender + "\",                            "
        +"     \"phone_number\": \"" + this.phoneNumber + "\",                 "
        +"     \"notifications\": \"" + this.notifications + "\",              "
        +"     \"terms_and_conditions\": \"" + this.termsAndConditions + "\",  "
        +"     \"brand\": \"" + this.brand + "\",                              "
        +"     \"locale\": \"" + this.locale + "\"                             "
        +"  }                                                                  "
        + "}";
    }

     /**
     * @param userPoolClientId
     * @param userPoolClientSecret
     * @param userName
     * @return
     */
    private String calculateSecretHash(final String userPoolClientId, final String userPoolClientSecret, final String userName) {
        final SecretKeySpec signingKey = new SecretKeySpec(userPoolClientSecret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256_ALGORITHM);

        try {
            final Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(userName.getBytes(StandardCharsets.UTF_8));
            final byte[] rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.encodeBase64(rawHmac));
        } catch (final Exception e) {
            throw new RuntimeException("Error while calculating secret hash");
        }
    }

}