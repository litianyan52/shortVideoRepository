package com.example.video_data.bean;

public class ResAddComment {
    private ResComment comment;

    /**
     * comment : {"id":299,"user_id":69,"type":"archives","aid":128,"pid":0,"content":"厉害呀！！我居然是1楼沙发，哈哈哈~","comments":0,"ip":"117.69.22.65","useragent":"PostmanRuntime-ApipostRuntime/1.1.0","subscribe":0,"createtime":1761377594,"updatetime":1761377594,"deletetime":null,"status":"normal","user":{"id":69,"nickname":"173****6169","avatar":"https://titok.fzqq.fun/uploads/20240826/50d42d478612bb3f289dd6258caa046b.jpeg","url":"/u/69"},"create_date":"0秒前"}
     * __token__ : 95271a73671009be9fec8a7167136bb4
     */

    private String __token__;
    public String get__token__() {
        return __token__;
    }

    public void set__token__(String __token__) {
        this.__token__ = __token__;
    }



    public ResComment getComment() {
        return comment;
    }

    public void setComment(ResComment comment) {
        this.comment = comment;
    }
}
