package io.ashimjk.annotationprocessor.endpoints;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static javax.tools.StandardLocation.CLASS_OUTPUT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointIO {

    private static final String ENDPOINT_FILE_NAME = "endpoint.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void writeToFile(Endpoints endpoints, Filer filer) throws IOException {
        String json = MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(endpoints.getRolesByEndpoint());

        FileObject fileObject = filer.createResource(CLASS_OUTPUT, "", ENDPOINT_FILE_NAME);

        try (OutputStream outputStream = fileObject.openOutputStream();
             PrintWriter printWriter = new PrintWriter(outputStream)) {
            printWriter.println(json);
        }
    }

    @SuppressWarnings("nullness")
    public static Endpoints readFromFile() throws IOException {
        String file = requireNonNull(EndpointIO.class.getClassLoader().getResource(ENDPOINT_FILE_NAME)).getFile();

        Map<String, Endpoints.RolesByMethodType> rolesByMethodType = MAPPER.readValue(Paths.get(file).toFile(), new TypeReference<>() {
        });

        return new Endpoints(rolesByMethodType);
    }

}
