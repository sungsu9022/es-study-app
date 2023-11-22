package com.starter.core.common.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.NullNode
import com.starter.core.common.serializer.LocalDateTimeDeserializer
import com.starter.core.common.serializer.LocalDateTimeSerializer
import mu.KotlinLogging
import java.io.IOException
import java.io.InputStream
import java.time.LocalDateTime

object JsonUtil {
    private val logger = KotlinLogging.logger {  }
    /** 기본 [ObjectMapper]  */
    private val DEFAULT_OBJECT_MAPPER: ObjectMapper

    init {
        val ob = ObjectMapper()
        ob.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        ob.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        // local date time 관련 셋팅 추가
        ob.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ob.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        val module = SimpleModule()
        module.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer())
        module.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer())
        ob.registerModule(module)
        DEFAULT_OBJECT_MAPPER = ob
    }

    /**
     * object를 json으로 만들어 리턴한다.
     * 예외가 발생하면 null을 리턴한다.
     *
     * @param mapper [ObjectMapper] : Json 처리 시 사용할 jackson의 [ObjectMapper] 정보 객체
     * @param object [Object] : json으로 변경할 객체 값
     * @return [String] object 값을 Json text로 생성한 값
     */
    fun toJson(mapper: ObjectMapper, `object`: Any): String? {
        return try {
            toJsonWithException(mapper, `object`)
        } catch (e: Exception) {
            logger.error("JsonUtils : Unable to serialize to json=" + e.message, `object`, e)
            null
        }
    }

    @Throws(JsonProcessingException::class)
    fun toJsonWithException(`object`: Any): String? {
        return toJsonWithException(DEFAULT_OBJECT_MAPPER, `object`)
    }

    @Throws(JsonProcessingException::class)
    fun toJsonWithException(mapper: ObjectMapper, `object`: Any): String {
        return mapper.writeValueAsString(`object`)
    }

    fun <T> toPrettyJson(input: T): String? {
        return try {
            DEFAULT_OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(input)
        } catch (e: JsonProcessingException) {
            logger.warn("Fail to convert JSON to String", e)
            null
        }
    }


    /**
     * object를 json으로 만들어 리턴한다. [ObjectMapper]는 기본으로 정의된 것을 사용한다.
     * 예외가 발생하면 null을 리턴한다.
     *
     * @param object [Object] : json으로 변경할 객체 값
     * @return [String] object 값을 Json text로 생성한 값
     */
    fun toJson(`object`: Any): String? {
        return toJson(DEFAULT_OBJECT_MAPPER, `object`)
    }

    /**
     * json을 object로 변환하여 리턴한다.
     * 예외가 발생하면 null을 리턴한다.
     *
     * @param mapper [ObjectMapper] : Json 처리 시 사용할 jackson의 [ObjectMapper] 정보 객체
     * @param json [String] : Json text
     * @param clazz [Class]<[T]> : json 값을 설정할 [Object] Class Type
     * @return [Object] : json의 내용을 populate한 객체
     */
    fun <T> toObject(mapper: ObjectMapper, json: String, clazz: Class<T>): T? {
        var `object`: T? = null
        try {
            `object` = mapper.readValue(json, clazz)

        } catch (e: Exception) {
            logger.error(e.message, json, e)
        }
        return `object`
    }


    /**
     * json을 object로 변환하여 리턴한다.
     * 예외가 발생하면 null을 리턴한다.
     *
     * @param mapper [ObjectMapper] : Json 처리 시 사용할 jackson의 [ObjectMapper] 정보 객체
     * @param json [String] : Json text
     * @param typeReference : json 값을 설정할 [Object] Class Type
     * @return [Object] : json의 내용을 populate한 객체
     */
    fun <T> toObject(mapper: ObjectMapper, json: String, typeReference: TypeReference<T>): T? {
        var `object`: T? = null
        try {
            `object` = mapper.readValue(json, typeReference)
        } catch (e: Exception) {
            logger.error(e.message, json, e)
        }
        return `object`
    }

    /**
     * json을 object로 변환하여 리턴한다.
     * 예외가 발생하면 null을 리턴한다.
     *
     * @param json [String] : Json text
     * @param typeReference : json 값을 설정할 [Object] Class Type
     * @return [Object] : json의 내용을 populate한 객체
     */
    fun <T> toObject(json: String, typeReference: TypeReference<T>): T? {
        return toObject(DEFAULT_OBJECT_MAPPER, json, typeReference)
    }

    /**
     * [JsonNode]를 object로 변환하여 리턴한다. [ObjectMapper]는 기본으로 정의된 것을 사용한다.
     * 예외가 발생하면 null을 리턴한다.
     *
     * @param jsonNode [JsonNode] : JsonNode 객체
     * @param clazz [Class]<[T]> : json 값을 설정할 [Object] Class Type
     * @return [Object] : jsonNode의 내용을 populate한 객체
     */
    fun <T> toObject(jsonNode: JsonNode, clazz: Class<T>): T? {
        return toObject(DEFAULT_OBJECT_MAPPER, jsonNode, clazz)
    }

    /**
     * 예외가 발생하면 empty list를 리턴한다.
     */
    fun <T> toListWithoutNull(jsonNode: JsonNode?, clazz: Class<T>?): List<T>? {
        return toList(ArrayList(), jsonNode, clazz)
    }

    /**
     * 예외가 발생하면 null을 리턴한다.
     */
    fun <T> toList(jsonNode: JsonNode?, clazz: Class<T>?): List<T>? {
        return toList(null, jsonNode, clazz)
    }

    /**
     * [JsonNode]를 list로 변환하여 리턴한다. [ObjectMapper]는 기본으로 정의된 것을 사용한다.
     * 예외가 발생하면 defaultValue를 리턴한다.
     *
     * @param defaultValue 예외 발생 시 리턴 할 기본 값
     * @param jsonNode [JsonNode] : JsonNode 객체
     * @param clazz [Class]<[T]> : json 값을 설정할 [Object] Class Type
     * @return [Object] : jsonNode의 내용을 populate한 list
     */
    fun <T> toList(defaultValue: List<T>?, jsonNode: JsonNode?, clazz: Class<T>?): List<T>? {
        val mapper = DEFAULT_OBJECT_MAPPER
        var listValue = defaultValue
        if (jsonNode != null) {
            val listType: JavaType = mapper!!.typeFactory.constructCollectionType(ArrayList::class.java, clazz)
            try {
                listValue = mapper.readValue(jsonNode.toString(), listType)
            } catch (e: Exception) {
                logger.error(e.message, jsonNode, e)
            }
        }
        return listValue
    }

    /**
     * Json 문자열을 [JsonNode]로 Parsing 하고,
     * parsing된 [JsonNode]로부터 list로 변환하여 리턴한다.
     * [ObjectMapper]는 기본으로 정의된 것을 사용한다.
     * 예외가 발생하면 empty list를 리턴한다.
     *
     * @param clazz [Class]<[T]> : json 값을 설정할 [Object] Class Type
     * @return [Object] : jsonNode의 내용을 populate한 list
     */
    fun <T> toList(json: String?, clazz: Class<T>?): List<T>? {
        val jsonNode = getJsonNode(json)
        return toList(jsonNode, clazz)
    }

    /**
     * [JsonNode]를 object로 변환하여 리턴한다.
     * 예외가 발생하면 null을 리턴한다.
     *
     * @param mapper [ObjectMapper] : Json 처리 시 사용할 jackson의 [ObjectMapper] 정보 객체
     * @param jsonNode [JsonNode] : JsonNode 객체
     * @param clazz [Class]<[T]> : json 값을 설정할 [Object] Class Type
     * @return [Object] : jsonNode의 내용을 populate한 객체
     */
    fun <T> toObject(mapper: ObjectMapper, jsonNode: JsonNode, clazz: Class<T>): T? {

        try {
            return mapper.readValue(jsonNode.toString(), clazz)
        } catch (e: Exception) {
            logger.error(e.message, jsonNode, e)
        }
        return null
    }

    /**
     * json을 기본 정의된 ObjectMapper를 이용하여 해당 클래스 객체를 생성해 리턴한다.
     * 예외가 발생하면 null을 리턴한다.
     *
     * @param json [String] : Json text
     * @param clazz [Class]<[T]> : json 값을 설정할 [Object] Class Type
     * @return [Object] : json의 내용을 populate한 객체
     */
    fun <T> toObject(json: String, clazz: Class<T>): T? {
        return toObject(DEFAULT_OBJECT_MAPPER, json, clazz)
    }


    /**
     * Json 문자열을 [JsonNode]로 Parsing 하여 반환한다.
     * Parsing된 [JsonNode]에서 [String]이나 [BigInteger] 값을 추출하는데 사용한다.
     *
     * @param json [String] : Json text
     * @return [JsonNode] : parsing된 JsonNode 객체
     * @see JsonNode
     */
    fun getJsonNode(json: String?): JsonNode {
        return try {
            DEFAULT_OBJECT_MAPPER.readTree(json)
        } catch (e: Exception) {
            logger.error("JsonUtils : [JSON Parsing Error]", e)
            NullNode.getInstance()
        }
    }

    /**
     * Json 문자열을 [JsonNode]로 Parsing 하여 반환한다.
     * Parsing된 [JsonNode]에서 [String]이나 [BigInteger] 값을 추출하는데 사용한다.
     *
     * @param json [InputStream] : Json text
     * @return [JsonNode] : parsing된 JsonNode 객체
     * @see JsonNode
     */
    fun getJsonNode(json: InputStream?): JsonNode? {
        return try {
            DEFAULT_OBJECT_MAPPER.readTree(json)
        } catch (e: Exception) {
            logger.error("JsonUtils : [JSON Parsing Error]", e)
            NullNode.getInstance()
        }
    }

    /**
     * Json에서 int값을 추출한다.<br></br>
     * [org.codehaus.jackson.node.NullNode.asInt]의 이상 작동 대안
     * TODO : 2017-07-27 jackson 버전을 2.7로 올렸기 때문에 여전히 비정상작동하는지 확인해봐야함
     *
     * @param node [JsonNode]
     * @param defaultValue int : 기본 값
     * @return int
     * @see .isEmptyNode
     */
    fun asInt(node: JsonNode, defaultValue: Int): Int {
        return if (isEmptyNode(node)) defaultValue else node.asInt(defaultValue)
    }

    /**
     * [JsonNode]에서 long값을 추출한다.<br></br>
     * [org.codehaus.jackson.node.NullNode.asLong]의 이상 작동 대안
     * TODO : 2017-07-27 jackson 버전을 2.7로 올렸기 때문에 여전히 비정상작동하는지 확인해봐야함
     *
     * @param node [JsonNode]
     * @param defaultValue long : 기본 값
     * @return long
     * @see .isEmptyNode
     */
    fun asLong(node: JsonNode, defaultValue: Long): Long {
        return if (isEmptyNode(node)) defaultValue else node.asLong(defaultValue)
    }

    /**
     * [JsonNode]에서 double값을 추출한다.<br></br>
     * [org.codehaus.jackson.node.NullNode.asDouble]의 이상 작동 대안
     * TODO : 2017-07-27 jackson 버전을 2.7로 올렸기 때문에 여전히 비정상작동하는지 확인해봐야함
     *
     * @param node [JsonNode]
     * @param defaultValue double : 기본 값
     * @return double
     * @see .isEmptyNode
     */
    fun asDouble(node: JsonNode, defaultValue: Double): Double {
        return if (isEmptyNode(node)) defaultValue else node.asDouble(defaultValue)
    }

    /**
     * 입력한 [JsonNode]의 Text 값을 JsonNode로 변환하여 반환한다.
     * <pre>
     * text value 자체가 Json text일때 text를 [JsonNode]의 tree 구조로 변한한다.
    </pre> *
     *
     * @param node [JsonNode]
     * @return [JsonNode]
     */
    fun asJsonNode(node: JsonNode): JsonNode? {
        return if (isEmptyNode(node)) {
            node
        } else {
            getJsonNode(node.asText())
        }
    }

    /**
     * [JsonNode]에서 boolean값을 추출한다.<br></br>
     * [org.codehaus.jackson.node.NullNode.asBoolean]의 이상 작동 대안
     * TODO : 2017-07-27 jackson 버전을 2.7로 올렸기 때문에 여전히 비정상작동하는지 확인해봐야함
     *
     * @param node [JsonNode]
     * @param defaultValue boolean : 기본 값
     * @return boolean
     * @see .isEmptyNode
     */
    fun asBoolean(node: JsonNode, defaultValue: Boolean): Boolean {
        return if (isEmptyNode(node)) defaultValue else node.asBoolean(defaultValue)
    }


    /**
     * 비어있거나 존재하지 않는 [JsonNode] 여부 반환
     *
     * @param node [JsonNode]
     * @return boolean
     * @see com.fasterxml.jackson.databind.node.NullNode
     *
     * @see com.fasterxml.jackson.databind.node.MissingNode
     */
    fun isEmptyNode(node: JsonNode?): Boolean {
        return node == null || node.isNull || node.isMissingNode
    }

    /**
     * 비어있거나 존재하지 않는 [JsonNode] 여부 반환
     *
     * @param node [JsonNode]
     * @return boolean
     */
    fun isNotEmptyNode(node: JsonNode?): Boolean {
        return !isEmptyNode(node)
    }

    /**
     * Map 내에서 Json 데이터를 찾는다.
     *
     * @param jsonMap 데이터 map
     * @param nodePathStr '>' 로 map 내에서 json의 경로를 설정
     *
     * nodePatStr에서 가장 나중에 있는 item을 Object로 캐스팅해서 반환
     */
    fun findData(
        jsonMap: Map<String, Any>,
        nodePathStr: String
    ): Any? {
        var currentNode = jsonMap
        val nodePath = nodePathStr.split(">".toRegex()).toTypedArray()
        for (i in 0 until nodePath.size - 1) {
            currentNode = currentNode[nodePath[i]] as Map<String, Any>
            if (currentNode == null) {
                return null
            }
        }
        return currentNode[nodePath[nodePath.size - 1]]
    }

    fun <T> convertValue(obj: Any?, clazz: Class<T>?): T {
        return DEFAULT_OBJECT_MAPPER.convertValue(obj, clazz) as T
    }

    /**
     * DEFAULT_OBJECT_MAPPER configure 설정
     * @param f
     * @param state
     */
    fun configure(f: JsonGenerator.Feature?, state: Boolean) {
        DEFAULT_OBJECT_MAPPER.configure(f, state)
    }

    /**
     * DEFAULT_OBJECT_MAPPER readTree
     * @param json
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun readTree(json: String?): JsonNode? {
        return DEFAULT_OBJECT_MAPPER.readTree(json)
    }

}
