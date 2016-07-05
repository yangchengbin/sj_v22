2.3版本数据库变更
新增record 视频记录表
goods表中新增seq字段 推荐顺序 默认99 即不排序
personage 表也添加seq字段   之前的person_order表废弃
story 表也添加seq字段    story表的type字段添加一个类型  3纯故事  之前的story_order废弃
story 表添加 阅读量view_count字段
crowdfunding 添加seq字段
search_history 添加 type字段
goods表添加 sale_count 销售数量



2.4版本数据库变更
新增 master_apply  大师申请表
新增大师申请相关图片表rel_pictures
personage表新增source字段
添加check_flow表审核过程表
crowdfunding表添加is_gy字段 是否为公益
crowdfunding_detail 表添加 type字段 1回报 2无偿 3抽奖