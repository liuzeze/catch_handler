/*
 * Copyright 2017 JessYan
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
package com.lz.fram.app;



import com.lz.fram.component.AppComponent;

import io.reactivex.annotations.NonNull;

/**
 * -----------作者----------日期----------变更内容-----
 * -          刘泽      2018-06-28       application 的接口
 */
public interface App {
    @NonNull
    AppComponent getAppComponent();
}
