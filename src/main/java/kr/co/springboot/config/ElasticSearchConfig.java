package kr.co.springboot.config;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
	
	@Value("${spring.elasticsearch.rest.uris}")
	private String elasticSearchUrl;
	
	/**
	 * Return the {@link RestHighLevelClient} instance used to connect to the cluster. <br />
	 * @return never {@literal null}.
	 */
	@Override
	@Bean
	public RestHighLevelClient elasticsearchClient() {
		final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
				.connectedTo(elasticSearchUrl)
				.build();
		
		return RestClients.create(clientConfiguration).rest();
	}
	
	@Bean
	public ElasticsearchOperations elasticsearchOperations() {
		return new ElasticsearchRestTemplate(elasticsearchClient());
	}
}