package community.flock.eco.feature.user.services

import community.flock.eco.feature.user.UserConfiguration
import community.flock.eco.feature.user.forms.UserForm
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [UserConfiguration::class])
@DataJpaTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userGroupService: UserGroupService

    @Test
    fun `create new user`() {
        val form = UserForm(
                name = "User 1",
                email = "user-1@gmail.com"
        )
        val user = userService.create(form)
        Assert.assertNotNull(user)
    }


    @Test
    fun `remove user`() {
        val form = UserForm(
                name = "User 2",
                email = "user-1@gmail.com"
        )
        val user = userService.create(form)
        Assert.assertNotNull(user)

        userService.delete(user.code)

        Assert.assertEquals(0, userService.findAll().count())
    }

}
