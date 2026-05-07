@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package moviles2026aswgr1.shared.generated.resources

import kotlin.OptIn
import org.jetbrains.compose.resources.StringResource

private object CommonMainString0 {
  public val saludo: StringResource by 
      lazy { init_saludo() }
}

internal val Res.string.saludo: StringResource
  get() = CommonMainString0.saludo

private fun init_saludo(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:saludo", "saludo",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/moviles2026aswgr1.shared.generated.resources/values/strings.commonMain.cvr",
    10, 42),
    )
)
