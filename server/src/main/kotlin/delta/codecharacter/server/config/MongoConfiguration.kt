package delta.codecharacter.server.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import javax.annotation.PostConstruct

@Configuration
class MongoConfiguration {
    @Autowired private lateinit var mappingMongoConverter: MappingMongoConverter

    @PostConstruct
    fun setUpMongoEscapeCharacterConversion() {
        mappingMongoConverter.setTypeMapper(DefaultMongoTypeMapper(null))
    }
}
