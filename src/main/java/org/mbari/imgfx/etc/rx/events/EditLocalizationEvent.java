/*
 * Copyright © 2025 MBARI (brian@mbari.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mbari.imgfx.etc.rx.events;

import javafx.scene.Node;
import org.mbari.imgfx.roi.Localization;
import org.mbari.imgfx.roi.Data;
import org.mbari.imgfx.roi.DataView;

public record EditLocalizationEvent(Localization<? extends DataView<? extends Data, ? extends Node>, ? extends Node> localization)
        implements Event {}
