package yong.petdoc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

	@Value("${kakao.api-key}")
	private String kakaoApiKey;

	@Bean
	public RestClient kakaoRestClient() {
		return RestClient.builder()
			.baseUrl("https://dapi.kakao.com/v2/local/search")
			.defaultHeader("Authorization", "KakaoAK " + kakaoApiKey)
			.build();
	}
}
