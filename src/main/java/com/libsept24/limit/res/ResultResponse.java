package com.libsept24.limit.res;import lombok.Getter;import lombok.Setter;import java.io.Serializable;import java.time.LocalDateTime;@Setter@Getterpublic class ResultResponse<T> implements Serializable {    private static final long serialVersionUID = -1133637474601003587L;    /**     * 接口响应时间     */    private LocalDateTime time = LocalDateTime.now();    /**     * 接口响应状态码     */    private Integer code;    /**     * 接口响应信息     */    private String msg;    /**     * 接口响应的数据     */    private T data;    /**     * 封装成功响应的方法     *     * @param data 响应数据     * @param <T>  响应数据类型     * @return reponse     */    public static <T> ResultResponse<T> success(T data) {        ResultResponse<T> response = new ResultResponse<>();        response.setData(data);        response.setCode(StatusEnum.SUCCESS.code);        return response;    }    /**     * 封装error的响应     *     * @param statusEnum error响应的状态值     * @param <T>     * @return     */    public static <T> ResultResponse<T> error(StatusEnum statusEnum) {        return error(statusEnum, statusEnum.message);    }    /**     * 封装error的响应  可自定义错误信息     *     * @param statusEnum error响应的状态值     * @param <T>     * @return     */    public static <T> ResultResponse<T> error(StatusEnum statusEnum, String errorMsg) {        ResultResponse<T> response = new ResultResponse<>();        response.setCode(statusEnum.code);        response.setMsg(errorMsg);        return response;    }}