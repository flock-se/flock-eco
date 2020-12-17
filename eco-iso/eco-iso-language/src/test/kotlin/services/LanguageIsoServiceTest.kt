package community.flock.eco.iso.language.services

import LanguageIsoConfiguration
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNull

@SpringBootTest(classes = [LanguageIsoConfiguration::class])
@AutoConfigureWebClient
class LanguageIsoServiceTest(
        private val languageIsoService: LanguageIsoService
) {

    @Test
    fun `find language by alpha-2 code`() {
        val language = languageIsoService.findByAlpha2("nl")
        assertEquals("Dutch; Flemish", language?.name)
        assertEquals("nl", language?.alpha2)
    }

    @Test
    fun `language not found`() {
        val language = languageIsoService.findByAlpha2("xx")
        assertNull(language)
    }

}
