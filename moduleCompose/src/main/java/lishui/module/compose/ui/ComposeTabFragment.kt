package lishui.module.compose.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import lishui.module.compose.R

/**
 *  author : linlishui
 *  time   : 2022/02/15
 *  desc   : Compose tab 页面
 */
class ComposeTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(context).inflate(
        R.layout.fragment_compose_tab_page, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeContainer = view.findViewById<ComposeView>(R.id.compose_container)
        composeContainer.setContent {
            HelloCompose()
        }
    }

    @Composable
    private fun HelloCompose() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Hello Compose",
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.8f)
                    .background(color = Color.LightGray, shape = CutCornerShape(12))
                    .padding(horizontal = dimensionResource(android.R.dimen.app_icon_size))
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            Text(
                text = "Test Layout",
                textAlign = TextAlign.Start,
                modifier = Modifier.background(color = Color.Magenta)
            )
        }

    }

    @Preview(showBackground = true)
    @Composable
    private fun PreviewGreeting() {
        HelloCompose()
    }
}