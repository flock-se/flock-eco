package community.flock.eco.feature.workspace.mappers

import community.flock.eco.feature.workspace.graphql.KeyValue
import community.flock.eco.feature.workspace.graphql.WorkspaceImageInput
import community.flock.eco.feature.workspace.graphql.WorkspaceInput
import community.flock.eco.feature.workspace.model.Workspace
import community.flock.eco.feature.workspace.model.WorkspaceImage
import community.flock.eco.feature.workspace.providers.WorkspaceUserProvider
import org.springframework.stereotype.Component
import java.util.*
import community.flock.eco.feature.workspace.graphql.Workspace as WorkspaceGraphql
import community.flock.eco.feature.workspace.graphql.WorkspaceUser as WorkspaceUserGraphql


@Component
class WorkspaceGraphqlMapper(
        private val workspaceUserProvider: WorkspaceUserProvider
) {


    fun consume(input: WorkspaceInput, workspace: Workspace? = null): Workspace = Workspace(
            id = workspace?.id ?: UUID.randomUUID(),
            name = input.name,
            variables = input.variables
                    .run { consume(this) },
            host = input.host,
            image = input.image
                    ?.run { consume(this) }
    )

    fun consume(it: List<KeyValue>): Map<String, String?> = it
            .map { it.key to it.value }
            .toMap()

    fun consume(it: WorkspaceImageInput) = WorkspaceImage(
            name = it.name,
            file = it.file.toByteArray()
    )

    fun produce(it: Workspace): WorkspaceGraphql = WorkspaceGraphql(
            id = it.id.toString(),
            name = it.name,
            variables = it.variables.map {
                KeyValue(it.key, it.value)
            },
            users = it.users
                    .map { it.id }
                    .let { workspaceUserProvider.findWorkspaceUser(it) }
                    .map { WorkspaceUserGraphql(
                            id = it.id,
                            name = it.name,
                            role = it.role
                    ) },
            host= it.host
    )

}
