package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;


public abstract class AbstractIntegrationTest {

    public abstract String getResourceName();

    static Client cliente;

    static WebTarget target;

    static Network red = Network.newNetwork();

    static MountableFile war = MountableFile.forHostPath(Paths.get("target/admision-api.war").toAbsolutePath());

    static GenericContainer postgres = new PostgreSQLContainer("postgres:16")
            .withDatabaseName("tpi135")
            .withInitScript("database.sql")
            .withPassword("abc123")
            .withUsername("postgres")
            .withNetwork(red)
            .withNetworkAliases("db")
            .withExposedPorts(5432)
            ;


    static GenericContainer liberty = new GenericContainer("openliberty-pg:10.26.0.0.2")
            .withEnv("PGSERVER", "db")
            .withEnv("PGPORT", "5432")
            .withEnv("PGDBNAME", "tpi135")
            .withEnv("PGUSER", "postgres")
            .withEnv("PGPASSWORD", "abc123")
            .dependsOn(postgres) // que no arranque si no ha arrancado la base de datos
            .withNetwork(red)
            .withCopyFileToContainer(war, "/home/usuario/wlp/usr/servers/app/dropins/nuevoingreso.war")
            .withExposedPorts(9080)
            .waitingFor(Wait.forHttp("/nuevoingreso").forPort(9080).forStatusCode(200)
            );

    static{
        postgres.start();
        liberty.start();
    }

    @BeforeAll
    public void inicializar(){
        Assertions.assertTrue(liberty.isRunning());
        cliente = ClientBuilder.newClient();
        target = cliente.target(String.format("http://localhost:%d/nuevoingreso/v1/%s", liberty.getMappedPort(9080), getResourceName()));
    }
}