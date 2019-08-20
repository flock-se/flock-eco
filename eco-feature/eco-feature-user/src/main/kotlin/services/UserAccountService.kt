package community.flock.eco.feature.user.services

import community.flock.eco.core.utils.toNullable
import community.flock.eco.feature.user.exceptions.UserAccountWithEmailExistsException
import community.flock.eco.feature.user.forms.UserAccountForm
import community.flock.eco.feature.user.forms.UserAccountOauthForm
import community.flock.eco.feature.user.forms.UserAccountPasswordForm
import community.flock.eco.feature.user.forms.UserForm
import community.flock.eco.feature.user.model.User
import community.flock.eco.feature.user.model.UserAccountOauth
import community.flock.eco.feature.user.model.UserAccountOauthProvider
import community.flock.eco.feature.user.model.UserAccountPassword
import community.flock.eco.feature.user.repositories.UserAccountOauthRepository
import community.flock.eco.feature.user.repositories.UserAccountPasswordRepository
import community.flock.eco.feature.user.repositories.UserAccountRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserAccountService(
        private val userService: UserService,
        private val passwordEncoder: PasswordEncoder,
        private val userAccountRepository: UserAccountRepository,
        private val userAccountOauthRepository: UserAccountOauthRepository,
        private val userAccountPasswordRepository: UserAccountPasswordRepository
) {

    fun findUserAccountPasswordByEmail(email: String) = userAccountPasswordRepository.findByUserEmail(email)
            .toNullable()

    fun findUserAccountOauthByReference(reference: String) = userAccountOauthRepository.findByReference(reference)
            .toNullable()

    fun createUserAccountPassword(email: String, name: String?, password: String): UserAccountPassword? = UserAccountPasswordForm(email, name, password)
            .let(this::createUserAccountPassword)


    fun createUserAccountPassword(form: UserAccountPasswordForm): UserAccountPassword? = userService.findByEmail(form.email)
            .let {
                if (it == null) {
                    createUser(form)
                } else {
                    throw UserAccountWithEmailExistsException(it)
                }
            }
            ?.let { form.internalize(it) }
            ?.let(userAccountRepository::save)

    fun createUserAccountOauth(email: String, name: String?, reference: String, provider: UserAccountOauthProvider): UserAccountOauth? = UserAccountOauthForm(email, name, reference, provider)
            .let(this::createUserAccountOauth)

    fun createUserAccountOauth(form: UserAccountOauthForm): UserAccountOauth? = userService.findByEmail(form.email)
            .let { createUser(form) }
            ?.let { form.internalize(it) }
            ?.let(userAccountRepository::save)

    private fun createUser(form: UserAccountForm) = UserForm(
            email = form.email,
            name = form.name)
            .let(userService::create)

    private fun UserAccountPasswordForm.internalize(user: User) = this
            .let {
                UserAccountPassword(
                        user = user,
                        password = passwordEncoder.encode(password)
                )
            }

    private fun UserAccountOauthForm.internalize(user: User) = this
            .let {
                UserAccountOauth(
                        user = user,
                        provider = it.provider,
                        reference = it.reference
                )
            }
}
