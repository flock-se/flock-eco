package community.flock.eco.application.example

import LanguageIsoConfiguration
import community.flock.eco.feature.member.MemberConfiguration
import community.flock.eco.feature.member.develop.data.MemberLoadData
import community.flock.eco.feature.user.UserConfiguration
import community.flock.eco.feature.user.develop.data.UserLoadData
import community.flock.eco.feature.workspace.WorkspaceConfiguration
import community.flock.eco.feature.workspace.develop.data.WorkspaceLoadData
import community.flock.eco.feature.workspace.providers.WorkspaceUserProvider
import community.flock.eco.iso.country.CountryIsoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@EntityScan
@ComponentScan
@Import(UserConfiguration::class,
        MemberConfiguration::class,
        CountryIsoConfiguration::class,
        LanguageIsoConfiguration::class,
        WorkspaceConfiguration::class)
class ApplicationConfiguration
