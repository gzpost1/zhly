package cn.cuiot.dmp.baseconfig.flow.config;

import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.cfg.HttpClientConfig;
import org.flowable.engine.impl.cfg.DelegateExpressionFieldInjectionMode;
import org.flowable.job.service.JobHandler;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import cn.cuiot.dmp.baseconfig.flow.flowable.job.IdWorkerIdGenerator;
import cn.cuiot.dmp.baseconfig.flow.flowable.job.CustomJobHandler;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @date
 * @desc
 */
@Configuration
public class FlowAbleConfig {
	@Resource
	private  DataSource dataSource;
	@Resource
	private  PlatformTransactionManager transactionManager;
	@Resource
	private IdWorkerIdGenerator idWorkerIdGenerator;
	@Bean
	public SpringProcessEngineConfiguration getSpringProcessEngineConfiguration() {
		SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
		config.setDisableEventRegistry(Boolean.TRUE);
		config.setActivityFontName("宋体");
		config.setAnnotationFontName("宋体");
		config.setLabelFontName("黑体");
		config.setDataSource(dataSource);
		config.setTransactionManager(transactionManager);
		config.setDisableIdmEngine(true);
		config.setDatabaseType("mysql");
		config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		config.setDelegateExpressionFieldInjectionMode(DelegateExpressionFieldInjectionMode.MIXED);
		config.setIdGenerator(idWorkerIdGenerator);
		config.setAsyncExecutorActivate(Boolean.TRUE);

//		config.setMailServerHost(flowableExConfig.getEmailHost());
//		config.setMailServerPort(flowableExConfig.getEmailPort());
//		config.setMailServerUsername(flowableExConfig.getEmailAddr());
//		config.setMailServerPassword(flowableExConfig.getEmailPassword());



		HttpClientConfig httpClientConfig=new HttpClientConfig();
		//连接超时
		httpClientConfig.setConnectTimeout(1000000);
		//连接请求超时
		httpClientConfig.setConnectionRequestTimeout(1000000);
		//双端建立socket连接
		httpClientConfig.setSocketTimeout(1000000);
		//请求失败之后重试次数
		httpClientConfig.setRequestRetryLimit(3);
		config.setHttpClientConfig(httpClientConfig);
		config.setKnowledgeBaseCacheLimit(200);
		config.setProcessDefinitionCacheLimit(200);
		List<JobHandler> customJobHandlers =new ArrayList<>();
		customJobHandlers.add(new CustomJobHandler());
		config.setCustomJobHandlers(customJobHandlers);
		return config;
	}

	@Bean
	@Primary
	public TaskExecutor primaryTaskExecutor() {
		return new ThreadPoolTaskExecutor();
	}

}
