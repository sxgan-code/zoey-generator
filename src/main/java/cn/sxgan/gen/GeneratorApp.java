package cn.sxgan.gen;

import cn.sxgan.gen.utils.FileUtils;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import lombok.extern.slf4j.Slf4j;

import java.sql.Types;
import java.util.Collections;
/**
* @Description: 主启动类
* @Author: sxgan
* @Date: 2024/3/5 12:58
* @Version: 1.0
**/
@Slf4j
public class GeneratorApp {
    public static void main(String[] args) {
        log.info("test logback");
        String baseDir = System.getProperty("user.dir");
        String outDir = baseDir + "/gen_code";
        FileUtils.delAllFile(outDir);
        // 相关配置请查看：https://baomidou.com/pages/981406/
        FastAutoGenerator.create("jdbc:mysql://192.168.0.200:3306/zoey_music_boot_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                        "daniel", "Daniel2118")
                .globalConfig(builder -> {
                    builder.author("sxgan") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(baseDir + "/gen_code"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder -> {
                    builder.parent("cn.sxgan") // 设置父包名
                            //.moduleName("gen") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, baseDir + "/gen_code/mapperxml")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_user") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                    builder
                            // Entity 策略配置
                            .entityBuilder()
                            .enableLombok()
                            .enableFileOverride()
                            .formatFileName("%s")
                            // Controller 策略配置
                            .controllerBuilder()
                            .enableFileOverride()
                            .enableRestStyle()
                            .formatFileName("%sController")
                            // Service 策略配置
                            .serviceBuilder()
                            .enableFileOverride()
                            .formatServiceFileName("I%sService")
                            .formatServiceImplFileName("%sServiceImp")
                            // Mapper 策略配置
                            .mapperBuilder()
                            .enableFileOverride()
                            .superClass(BaseMapper.class)
                            .enableMapperAnnotation()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapper");

                })
                //.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}