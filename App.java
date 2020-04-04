import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.ClientProtocolException;

public class App {

    final static String COGNITO_URL = "https://cognito-idp.eu-west-1.amazonaws.com";

    public static void main(final String[] args) {

        final Payload payload = new Payload();
        payload.setClientId("");
        payload.setClientSecret("");
        payload.setEmail("");
        payload.setPassword("");
        payload.setFirstName("");
        payload.setLastName("");
        payload.setBirthdate("");
        payload.setPhoneNumber("");
        payload.setGender("f");
        payload.setNotifications("");
        final String data = payload.build();

        try {
            send(data);
        } catch (final Exception exception) {
            System.out.println("Exception: \n" + exception.getMessage());
        }
    }

    /**
     * Send the registration request
     * 
     * @param payload
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void send(final String payload) throws ClientProtocolException, IOException {

        final HttpPost httpPost = new HttpPost(COGNITO_URL);
        httpPost.setEntity(new StringEntity(payload));

        httpPost.setHeader("Content-type", "application/x-amz-json-1.1");
        httpPost.setHeader("X-Amz-Target", "AWSCognitoIdentityProviderService.SignUp");

        final HttpClient httpClient = HttpClientBuilder.create().build();
        final HttpResponse response = httpClient.execute(httpPost);

        // Create the StringBuffer object and store the response into it.
        final BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        final StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = br.readLine()) != null) {
            System.out.println("Response : \n" + result.append(line));
        }
    }
}