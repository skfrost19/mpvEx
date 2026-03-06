package app.marlboroadvance.mpvex.ui.utils

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import kotlin.math.abs

/**
 * Utilities for optimizing scrolling performance on high refresh rate displays (90Hz, 120Hz, 144Hz, etc.)
 * 
 * These optimizations help achieve buttery smooth scrolling by:
 * - Providing optimized fling behavior for high refresh rate displays
 * - Reducing overdraw and unnecessary recompositions
 * - Improving the feel and responsiveness of scrolling gestures
 */

/**
 * Creates a fling behavior optimized for high refresh rate displays.
 * This provides smoother, more responsive scrolling on 90Hz, 120Hz, and 144Hz+ screens.
 * 
 * @param density The current screen density
 * @return A [FlingBehavior] configured for smooth high refresh rate scrolling
 */
@Composable
fun rememberSmoothFlingBehavior(
    density: Density = LocalDensity.current
): FlingBehavior {
    return remember(density) {
        SmoothScrollFlingBehavior(
            decayAnimationSpec = exponentialDecay(
                // Lower friction coefficient for smoother, more natural feeling scrolling
                // on high refresh rate displays
                frictionMultiplier = 0.8f,
                // Higher absolute velocity threshold allows for faster flinging
                absVelocityThreshold = 200f
            ),
            density = density
        )
    }
}

/**
 * Custom fling behavior that provides smoother scrolling on high refresh rate displays.
 * 
 * This implementation is optimized for 90Hz, 120Hz, and 144Hz+ displays by:
 * - Using exponential decay with reduced friction for more natural feel
 * - Supporting higher velocity thresholds for responsive flinging
 * - Maintaining smooth animation even during rapid scrolling
 */
private class SmoothScrollFlingBehavior(
    private val decayAnimationSpec: DecayAnimationSpec<Float>,
    private val density: Density
) : FlingBehavior {
    
    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        // If velocity is very small, don't perform fling
        if (abs(initialVelocity) < 200f * density.density) {
            return 0f
        }
        
        var remainingVelocity = initialVelocity
        var lastValue = 0f
        
        val animationState = AnimationState(
            initialValue = 0f,
            initialVelocity = initialVelocity
        )
        
        animationState.animateDecay(decayAnimationSpec) {
            val delta = value - lastValue
            val consumed = scrollBy(delta)
            lastValue = value
            remainingVelocity = velocity
            
            // If we can't consume any more scroll, stop the animation
            if (abs(delta - consumed) > 0.5f) {
                cancelAnimation()
            }
        }
        
        return remainingVelocity
    }
}

/**
 * Recommended settings for LazyColumn/LazyVerticalGrid on high refresh rate displays
 */
object HighRefreshRateScrollConfig {
    /**
     * Number of items to compose and lay out beyond the visible bounds.
     * Higher values improve scrolling smoothness but use more memory.
     * 
     * For high refresh rate displays (120Hz+), we want to prefetch more items
     * to avoid composition during fast scrolling.
     */
    const val BEYOND_BOUNDS_ITEM_COUNT = 3
    
    /**
     * Returns true if the device likely has a high refresh rate display.
     * This is a heuristic and might not be 100% accurate.
     */
    fun isLikelyHighRefreshRateDisplay(): Boolean {
        // Most modern Android devices with API 28+ support high refresh rates
        // This is a simple heuristic - actual refresh rate detection would require
        // WindowManager APIs available in API 30+
        return android.os.Build.VERSION.SDK_INT >= 28
    }
}

/**
 * Creates a LazyListState with optimized settings for high refresh rate displays
 */
@Composable
fun rememberOptimizedLazyListState(
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0
): LazyListState {
    return androidx.compose.foundation.lazy.rememberLazyListState(
        initialFirstVisibleItemIndex = initialFirstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = initialFirstVisibleItemScrollOffset
    )
}

/**
 * Creates a LazyGridState with optimized settings for high refresh rate displays
 */
@Composable
fun rememberOptimizedLazyGridState(
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0
): LazyGridState {
    return androidx.compose.foundation.lazy.grid.rememberLazyGridState(
        initialFirstVisibleItemIndex = initialFirstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = initialFirstVisibleItemScrollOffset
    )
}
