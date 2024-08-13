package com.shanzhu.travel.controller;

import com.shanzhu.travel.common.Query;
import com.shanzhu.travel.entity.Shoucangjilu;
import com.shanzhu.travel.service.ShoucangjiluService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tk.mybatis.mapper.entity.Example;
import com.shanzhu.travel.util.Info;
import com.shanzhu.travel.util.Request;

import javax.annotation.Resource;
import java.util.List;

/**
 * 收藏 控制层
 *
 * @author: ShanZhu
 * @date: 2024-01-26
 */
@Controller
public class ShoucangjiluController extends BaseController {

    @Resource
    protected ShoucangjiluService shoucangjiluService;

    /**
     * 加入收藏
     *
     * @return
     */
    @RequestMapping("/collect")
    public String collect() {
        if (!checkLogin()) {
            return showError("您尚未登录请登录后在操作");
        }
        int id = Request.getInt("id");
        String biao = request.getParameter("biao");
        String ziduan = request.getParameter("ziduan");
        Shoucangjilu scjl = new Shoucangjilu();
        scjl.setXwid(id);
        scjl.setBiao(biao);
        scjl.setBiaoti(Query.make(biao).where("id", id).value(ziduan));
        scjl.setUrl(request.getHeader("referer"));
        scjl.setZiduan(ziduan);
        scjl.setAddtime(Info.getDateStr());
        scjl.setUsername(request.getSession().getAttribute("username").toString());
        shoucangjiluService.insert(scjl);
        return showSuccess("收藏成功", request.getHeader("referer"));
    }

    /**
     * 查看收藏列表
     *
     * @return
     */
    @RequestMapping("/shoucangjilu_list")
    public String index() {
        int page = Math.max(Integer.valueOf(Request.get("page", "1")), 1);
        Example example = new Example(Shoucangjilu.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("username", request.getSession().getAttribute("username"));
        if (isAjax()) {
            return json();
        }
        return "shoucangjilu_list";
    }

    /**
     * 查看我的收藏
     *
     * @return
     */
    @RequestMapping("/shoucangjilu_list2")
    public String index2() {
        int page = Math.max(Integer.valueOf(Request.get("page", "1")), 1);
        Example example = new Example(Shoucangjilu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", request.getSession().getAttribute("username"));
        List<Shoucangjilu> list = shoucangjiluService.selectPageExample(example, page, 15);
        assign("list", list);
        if (isAjax()) {
            return json();
        }
        return "shoucangjilu_list";
    }

    /**
     * 删除收藏
     *
     * @return
     */
    @RequestMapping("/shoucangjilu_delete")
    public String delete() {
        String id = Request.get("id");
        shoucangjiluService.delete(id);
        return showSuccess("删除成功");
    }

}
