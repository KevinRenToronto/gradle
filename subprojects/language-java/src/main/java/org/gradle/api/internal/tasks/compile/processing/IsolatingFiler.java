/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.tasks.compile.processing;

import org.gradle.api.internal.tasks.compile.incremental.processing.AnnotationProcessingResult;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import java.util.Set;

/**
 * Decorates the filer to validate the correct behavior for {@link IsolatingProcessor}s.
 */
class IsolatingFiler extends IncrementalFiler {

    IsolatingFiler(Filer delegate, AnnotationProcessingResult result) {
        super(delegate, result);
    }

    @Override
    protected void recordGeneratedType(CharSequence name, Element[] originatingElements) {
        String generatedType = name.toString();
        Set<String> originatingTypes = ElementUtils.getTopLevelTypeNames(originatingElements);
        int size = originatingTypes.size();
        if (size != 1) {
            result.setFullRebuildCause("the generated type '" + generatedType + "' must have exactly one originating element, but had " + size);
        }
        result.addGeneratedType(generatedType, originatingTypes);
    }

}
