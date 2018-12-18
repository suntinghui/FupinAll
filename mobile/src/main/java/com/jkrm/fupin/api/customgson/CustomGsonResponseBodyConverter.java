/*
 * Copyright (C) 2015 Square, Inc.
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
package com.jkrm.fupin.api.customgson;

import com.google.gson.TypeAdapter;
import com.jkrm.fupin.bean.ResponseBean;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            T t = adapter.fromJson(value.charStream());
            if (t != null && t instanceof ResponseBean) {
                ResponseBean baseEntity = (ResponseBean) t;
              //  sendEventBusPost(baseEntity);
            }
            return t;
        } finally {
            value.close();
        }
    }
    /**
     * 发布登录信息已过期通知
     */
    private void sendEventBusPost(ResponseBean baseEntity) {
        if (baseEntity == null) {
            return;
        }
     /*  if (baseEntity.result == Constant.CALLBACK_CODE_TOKEN_FAILURE){
            EventBus.getDefault().post(new TokenInvaliMessage(baseEntity.result, baseEntity.msg));
        }
*/
    }
}
