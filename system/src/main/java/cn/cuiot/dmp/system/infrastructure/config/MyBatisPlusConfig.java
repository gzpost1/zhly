package cn.cuiot.dmp.system.infrastructure.config;


import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import java.util.List;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlus配置类
 *
 * @author lixf
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public ISqlInjector iSqlInjector() {
        return new DefaultSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
                List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
                methodList.add(new DeleteByIdWithAllField());

                return methodList;
            }
        };
    }

    public class DeleteByIdWithAllField extends AbstractMethod {

        public DeleteByIdWithAllField() {
            this(SqlMethod.DELETE_BY_ID.getMethod() + "WithAllField");
        }

        /**
         * @param name 方法名
         * @since 3.5.0
         */
        public DeleteByIdWithAllField(String name) {
            super(name);
        }

        @Override
        public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
            String sql;
            SqlMethod sqlMethod = SqlMethod.LOGIC_DELETE_BY_ID;
            if (tableInfo.isWithLogicDelete()) {
                List<TableFieldInfo> fieldInfos = tableInfo.getFieldList().stream()
                        .filter(f -> !f.isLogicDelete())
                        .collect(toList());
                if (CollectionUtils.isNotEmpty(fieldInfos)) {
                    String sqlSet = "SET " + SqlScriptUtils.convertIf(fieldInfos.stream()
                            .map(i -> i.getSqlSet(EMPTY)).collect(joining(EMPTY)), "!@org.apache.ibatis.type.SimpleTypeRegistry@isSimpleType(_parameter.getClass())", true)
                            + tableInfo.getLogicDeleteSql(false, false);
                    sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlSet, tableInfo.getKeyColumn(),
                            tableInfo.getKeyProperty(), tableInfo.getLogicDeleteSql(true, true));
                } else {
                    sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlLogicSet(tableInfo),
                            tableInfo.getKeyColumn(), tableInfo.getKeyProperty(),
                            tableInfo.getLogicDeleteSql(true, true));
                }
                SqlSource sqlSource = super.createSqlSource(configuration, sql, Object.class);
                return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
            } else {
                sqlMethod = SqlMethod.DELETE_BY_ID;
                sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), tableInfo.getKeyColumn(),
                        tableInfo.getKeyProperty());
                return this.addDeleteMappedStatement(mapperClass, methodName, super.createSqlSource(configuration, sql, Object.class));
            }
        }
    }

}