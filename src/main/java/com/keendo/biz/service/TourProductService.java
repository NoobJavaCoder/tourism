package com.keendo.biz.service;

import com.keendo.architecture.exception.BizException;
import com.keendo.biz.mapper.TourProductMapper;
import com.keendo.biz.model.TourProduct;
import com.keendo.biz.service.bean.tourproduct.AddTourProduct;
import com.keendo.biz.service.bean.tourproduct.AdminTourProductItemResp;
import com.keendo.biz.service.bean.tourproduct.AdminTourProductListItemResp;
import com.keendo.biz.service.bean.tourproduct.TourProductItem;
import com.keendo.biz.service.utils.BeanUtils;
import com.keendo.biz.service.utils.ListUtil;
import com.keendo.biz.service.utils.TimeUtils;
import com.keendo.user.model.User;
import com.keendo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TourProductService {

    @Autowired
    private TourProductMapper tourProductMapper;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private TourOrderService tourOrderService;

    @Autowired
    private UserService userService;


    public List<TourProductItem> getAppTourProductItemList(Integer startIndex, Integer pageSize) {
        //未下架产品
        List<TourProduct> tourProductList = tourProductMapper.selectByPageAndState(startIndex, pageSize, Constants.UNSHELVE_STATE);

        List<TourProductItem> items = new ArrayList<>();

        if (ListUtil.isNotEmpty(tourProductList)) {

            for (TourProduct tourProduct : tourProductList) {

                TourProductItem item = BeanUtils.copyBean(tourProduct, TourProductItem.class);

                //结束时间
                Date departureTime = tourProduct.getDepartureTime();
                Integer tourDay = tourProduct.getTourDay();
                Date endTime = TimeUtils.dateOffset(departureTime, tourDay);
                item.setEndTime(endTime);

                //已下单人数
                Integer tourProductId = tourProduct.getId();
                Integer hasOrderedNum = tourOrderService.countByTourProductId(tourProductId);
                item.setHasOrderedNum(hasOrderedNum);

                //剩余人数
                Integer maxParticipantNum = tourProduct.getMaxParticipantNum();//人数上限
                item.setRemainNum(maxParticipantNum - hasOrderedNum);

                //参与者头像
                List<Integer> orderedUserIdList = tourOrderService.getOrderedUserIdList(tourProductId);
                List<String> headImgList = new ArrayList<>();
                if (ListUtil.isNotEmpty(orderedUserIdList)) {
                    for (Integer userId : orderedUserIdList) {
                        User user = userService.getById(userId);
                        if (user != null) {
                            String headImg = user.getHeadImgUrl();
                            headImgList.add(headImg);
                        }
                    }
                }
                item.setHeadImgList(headImgList);

                items.add(item);
            }
        }

        return items;
    }

    /**
     * 首页产品总数,未下架状态的产品
     *
     * @return
     */
    public Integer indexCount() {
        return tourProductMapper.countByState(Constants.UNSHELVE_STATE);
    }

    public List<AdminTourProductListItemResp> getListByPage(Integer startIndex, Integer pageSize) {
        List<TourProduct> tourProductList = tourProductMapper.selectByPage(startIndex, pageSize);

        List<AdminTourProductListItemResp> resps = new ArrayList<>();

        if (ListUtil.isNotEmpty(tourProductList)) {

            for (TourProduct tourProduct : tourProductList) {

                AdminTourProductListItemResp resp = BeanUtils.copyBean(tourProduct, AdminTourProductListItemResp.class);

                Integer id = tourProduct.getId();

                Integer count = tourOrderService.countByTourProductId(id);//已报名人数

                resp.setHasEnteredNum(count);

                resps.add(resp);
            }
        }

        return resps;
    }

    public Integer count() {
        return tourProductMapper.count();
    }

    /**
     * 上传图片
     *
     * @param multipartFile
     * @return
     */
    public String uploadPic(MultipartFile multipartFile, String directory) {

        return uploadService.uploadPic(multipartFile, directory, String.valueOf(System.currentTimeMillis()));
    }

    public TourProduct getById(Integer id) {
        return tourProductMapper.selectById(id);
    }


    /**
     * 获取admin查看旅游产品信息对象
     *
     * @param id
     */
    public AdminTourProductItemResp getAdminTourProductItemById(Integer id) {

        TourProduct tourProduct = this.getById(id);

        AdminTourProductItemResp resp = BeanUtils.copyBean(tourProduct, AdminTourProductItemResp.class);

        Integer hasPaidNum = tourOrderService.countHasPaidByTourProductId(id);
        resp.setHasPaidNum(hasPaidNum);//已付款人数

        Integer unPaidNum = tourOrderService.countUnPaidByTourProductId(id);
        resp.setUnPaidNum(unPaidNum);//未付款人数

        return resp;

    }

    public Integer add(AddTourProduct addTourProduct) {
        TourProduct tourProduct = BeanUtils.copyBean(addTourProduct, TourProduct.class);

        tourProduct.setCreateTime(new Date());
        tourProduct.setState(Constants.ON_GOING_STATE);

        return this.save(tourProduct);
    }

    public Integer update(TourProduct tourProduct) {
        return tourProductMapper.updateByPrimaryKey(tourProduct);
    }

    public Integer deleteById(Integer id) {
        return tourProductMapper.deleteById(id);
    }

    private Integer save(TourProduct tourProduct) {
        return tourProductMapper.insert(tourProduct);
    }

    /**
     * 获取所有正在进行中状态的产品List
     *
     * @return
     */
    public List<TourProduct> getOnGoingStateList() {
        return tourProductMapper.selectByState(Constants.ON_GOING_STATE);
    }

    /**
     * 更新旅游产品的状态
     *
     * @param id
     */
    public void updateStateById(Integer id, Integer state) {
        tourProductMapper.updateStateById(state, id);
    }

    /**
     * 修改为满员状态
     *
     * @param id
     * @return
     */
    public Integer fullState(Integer id) {
        return this.updateStateByIdAndFromState(id, Constants.ON_GOING_STATE, Constants.FULL_STATE);
    }

    private Integer updateStateByIdAndFromState(Integer id, Integer fromState, Integer toState) {
        return tourProductMapper.updateStateByIdAndFromState(id, fromState, toState);
    }

    public static class Constants {

        public final static String COVER_DIRECTORY_NAME = "/tourism/product/coverImg";//旅游产品封面图存放路径
        public final static String POSTER1_DIRECTORY_NAME = "/tourism/product/poster/share";//旅游产品海报存放路径
        public final static String POSTER2_DIRECTORY_NAME = "/tourism/product/poster/top-bar";//旅游产品海报存放路径

        public final static Integer ON_GOING_STATE = 1;//进行中
        public final static Integer FULL_STATE = 5;//满员
        public final static Integer FINISH_STATE = 9;//旅游结束
        public final static Integer UNSHELVE_STATE = 13;//下架

    }
}
