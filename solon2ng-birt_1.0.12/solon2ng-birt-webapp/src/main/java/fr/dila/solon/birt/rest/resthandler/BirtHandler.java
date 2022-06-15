package fr.dila.solon.birt.rest.resthandler;

import fr.dila.solon.birt.SolonBirtGenerator;
import fr.dila.solon.birt.common.RunResult;
import fr.dila.solon.birt.common.SolonBirtParameters;
import fr.dila.solon.birt.rest.config.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

@RestController
@RequestMapping("/api")
public class BirtHandler {

    @Value("${solon.birt.versionmode}")
    private String version;

    private final SolonBirtGenerator solonBirtGenerator;
    private final Properties properties;
    private final ConnectionManager connectionManager;

    public BirtHandler(SolonBirtGenerator solonBirtGenerator, Properties properties, ConnectionManager connectionManager) {
        this.solonBirtGenerator = solonBirtGenerator;
        this.properties = properties;
        this.connectionManager = connectionManager;
    }

    @GetMapping("/version")
    public String getVersion() {
        return version;
    }

    @PostMapping("/report")
    public ResponseEntity<?> generateReport(@RequestBody SolonBirtParameters solonBirtParameters) {
        RunResult result = solonBirtGenerator.run(properties, solonBirtParameters, connectionManager.getConnection());

        if (result.getStatus() == SolonBirtGenerator.CODE_OK) {
            return new ResponseEntity<>(result.getFilename(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
