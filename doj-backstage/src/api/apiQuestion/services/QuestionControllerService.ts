/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_List_QuestionTag_ } from '../models/BaseResponse_List_QuestionTag_';
import type { BaseResponse_List_Tag_ } from '../models/BaseResponse_List_Tag_';
import type { BaseResponse_long_ } from '../models/BaseResponse_long_';
import type { BaseResponse_Page_Question_ } from '../models/BaseResponse_Page_Question_';
import type { BaseResponse_Page_QuestionSubmitVO_ } from '../models/BaseResponse_Page_QuestionSubmitVO_';
import type { BaseResponse_Page_QuestionVO_ } from '../models/BaseResponse_Page_QuestionVO_';
import type { BaseResponse_Question_ } from '../models/BaseResponse_Question_';
import type { BaseResponse_QuestionSubmitVO_ } from '../models/BaseResponse_QuestionSubmitVO_';
import type { BaseResponse_QuestionTag_ } from '../models/BaseResponse_QuestionTag_';
import type { BaseResponse_QuestionVO_ } from '../models/BaseResponse_QuestionVO_';
import type { BaseResponse_Tag_ } from '../models/BaseResponse_Tag_';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { QuestionAddDTO } from '../models/QuestionAddDTO';
import type { QuestionEditDTO } from '../models/QuestionEditDTO';
import type { QuestionQueryDTO } from '../models/QuestionQueryDTO';
import type { QuestionSubmitAddDTO } from '../models/QuestionSubmitAddDTO';
import type { QuestionSubmitQueryDTO } from '../models/QuestionSubmitQueryDTO';
import type { QuestionTag } from '../models/QuestionTag';
import type { QuestionTagUpdateDTO } from '../models/QuestionTagUpdateDTO';
import type { QuestionUpdateDTO } from '../models/QuestionUpdateDTO';
import type { Tag } from '../models/Tag';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class QuestionControllerService {
    /**
     * addQuestion
     * @param questionAddRequest questionAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addQuestionUsingPost(
        questionAddRequest: QuestionAddDTO,
    ): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/add',
            body: questionAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * deleteQuestion
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteQuestionUsingPost(
        deleteRequest: DeleteRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * editQuestion
     * @param questionEditRequest questionEditRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static editQuestionUsingPost(
        questionEditRequest: QuestionEditDTO,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/edit',
            body: questionEditRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * getQuestionById
     * @param id id
     * @returns BaseResponse_Question_ OK
     * @throws ApiError
     */
    public static getQuestionByIdUsingGet(
        id?: number,
    ): CancelablePromise<BaseResponse_Question_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/get',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * getQuestionSubmitVOById
     * @param id id
     * @returns BaseResponse_QuestionSubmitVO_ OK
     * @throws ApiError
     */
    public static getQuestionSubmitVoByIdUsingGet(
        id?: number,
    ): CancelablePromise<BaseResponse_QuestionSubmitVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/get/submit/vo',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * getQuestionVOById
     * @param id id
     * @returns BaseResponse_QuestionVO_ OK
     * @throws ApiError
     */
    public static getQuestionVoByIdUsingGet(
        id?: number,
    ): CancelablePromise<BaseResponse_QuestionVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/get/vo',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * listQuestionByPage
     * @param questionQueryRequest questionQueryRequest
     * @returns BaseResponse_Page_Question_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listQuestionByPageUsingPost(
        questionQueryRequest: QuestionQueryDTO,
    ): CancelablePromise<BaseResponse_Page_Question_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/list/page',
            body: questionQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * listQuestionVOByPage
     * @param questionQueryRequest questionQueryRequest
     * @returns BaseResponse_Page_QuestionVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listQuestionVoByPageUsingPost(
        questionQueryRequest: QuestionQueryDTO,
    ): CancelablePromise<BaseResponse_Page_QuestionVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/list/page/vo',
            body: questionQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * listMyQuestionVOByPage
     * @param questionQueryRequest questionQueryRequest
     * @returns BaseResponse_Page_QuestionVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listMyQuestionVoByPageUsingPost(
        questionQueryRequest: QuestionQueryDTO,
    ): CancelablePromise<BaseResponse_Page_QuestionVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/my/list/page/vo',
            body: questionQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * addQuestionTag
     * @param questionTag questionTag
     * @returns BaseResponse_QuestionTag_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addQuestionTagUsingPost(
        questionTag: QuestionTag,
    ): CancelablePromise<BaseResponse_QuestionTag_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/question/tag/add',
            body: questionTag,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * deleteQuestionTag
     * @param questionTag questionTag
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteQuestionTagUsingPost(
        questionTag: QuestionTag,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/question/tag/delete',
            body: questionTag,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * getQuestionTagByQuetsionId
     * @param id id
     * @returns BaseResponse_List_QuestionTag_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static getQuestionTagByQuetsionIdUsingPost(
        id?: number,
    ): CancelablePromise<BaseResponse_List_QuestionTag_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/question/tag/getByQuestionId',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * getQuestionTagByTagId
     * @param id id
     * @returns BaseResponse_List_QuestionTag_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static getQuestionTagByTagIdUsingPost(
        id?: number,
    ): CancelablePromise<BaseResponse_List_QuestionTag_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/question/tag/getByTagId',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * updateQuestionTag
     * @param questionTagUpdateDto questionTagUpdateDTO
     * @returns BaseResponse_QuestionTag_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateQuestionTagUsingPost(
        questionTagUpdateDto: QuestionTagUpdateDTO,
    ): CancelablePromise<BaseResponse_QuestionTag_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/question/tag/update',
            body: questionTagUpdateDto,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * doQuestionSubmit
     * @param questionSubmitAddRequest questionSubmitAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static doQuestionSubmitUsingPost(
        questionSubmitAddRequest: QuestionSubmitAddDTO,
    ): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/question_submit/do',
            body: questionSubmitAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * listQuestionSubmitByPage
     * @param questionSubmitQueryRequest questionSubmitQueryRequest
     * @returns BaseResponse_Page_QuestionSubmitVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listQuestionSubmitByPageUsingPost(
        questionSubmitQueryRequest: QuestionSubmitQueryDTO,
    ): CancelablePromise<BaseResponse_Page_QuestionSubmitVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/question_submit/list/page',
            body: questionSubmitQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * addTag
     * @param name name
     * @returns BaseResponse_Tag_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addTagUsingPost(
        name?: string,
    ): CancelablePromise<BaseResponse_Tag_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/tag/add',
            query: {
                'name': name,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * deleteTag
     * @param id id
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteTagUsingPost(
        id?: number,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/tag/delete',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * getTagById
     * @param id id
     * @returns BaseResponse_Tag_ OK
     * @throws ApiError
     */
    public static getTagByIdUsingGet(
        id?: number,
    ): CancelablePromise<BaseResponse_Tag_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/tag/getById',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * getTagByName
     * @param name name
     * @returns BaseResponse_Tag_ OK
     * @throws ApiError
     */
    public static getTagByNameUsingGet(
        name?: string,
    ): CancelablePromise<BaseResponse_Tag_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/tag/getByName',
            query: {
                'name': name,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * listTags
     * @returns BaseResponse_List_Tag_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTagsUsingPost(): CancelablePromise<BaseResponse_List_Tag_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/tag/list',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * updateTag
     * @param tag tag
     * @returns BaseResponse_Tag_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateTagUsingPost(
        tag: Tag,
    ): CancelablePromise<BaseResponse_Tag_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/tag/update',
            body: tag,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * updateQuestion
     * @param questionUpdateRequest questionUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateQuestionUsingPost(
        questionUpdateRequest: QuestionUpdateDTO,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/update',
            body: questionUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
}
