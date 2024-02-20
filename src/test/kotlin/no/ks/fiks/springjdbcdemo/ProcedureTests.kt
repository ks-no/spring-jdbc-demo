package no.ks.fiks.springjdbcdemo

import io.kotest.core.spec.style.StringSpec
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location
import org.flywaydb.core.api.MigrationVersion
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.SqlParameter
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcCall
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.testcontainers.containers.MSSQLServerContainer
import org.testcontainers.utility.DockerImageName
import java.nio.charset.StandardCharsets
import java.sql.Types
import java.util.*

@SpringBootTest
class ProcedureTests : StringSpec() {

	private lateinit var dataSource: SingleConnectionDataSource

	init {
		beforeSpec {
			val mssqlServerContainer = MSSQLServerContainer(
				DockerImageName.parse("mcr.microsoft.com/mssql/server:2017-latest")
					.asCompatibleSubstituteFor("mcr.microsoft.com/mssql/server")
			).withPassword("Test1234!")

			mssqlServerContainer.start()

			Flyway.configure()
				.dataSource(mssqlServerContainer.jdbcUrl, mssqlServerContainer.username, mssqlServerContainer.password)
				.encoding(StandardCharsets.UTF_8)
				.target(MigrationVersion.LATEST)
				.defaultSchema("schemademo")
				.schemas("schemademo")
				.locations(Location("classpath:migrations"))
				.validateOnMigrate(true)
				.load()
				.migrate()

			dataSource = SingleConnectionDataSource(
				mssqlServerContainer.jdbcUrl,
				mssqlServerContainer.username,
				mssqlServerContainer.password,
				true
			)
		}

		"Test at ressurs eksisterer etter den er opprettet" {
			val newResource = UUID.randomUUID()
			val name = "Demo resource"
			val newName = "Updated resource"

			NamedParameterJdbcTemplate(dataSource)
				.update(
					"INSERT schemademo.resource (id, name) VALUES (:id, :name);",
					mapOf("id" to newResource, "name" to name)
				)

			SimpleJdbcCall(dataSource)
				.withSchemaName("schemademo")
				.withProcedureName("updateresource")
				.declareParameters(
					SqlParameter("id", Types.VARCHAR),
					SqlParameter("name", Types.VARCHAR)
				)
				.execute(MapSqlParameterSource(mapOf("id" to newResource, "name" to newName)))
		}
	}
}
