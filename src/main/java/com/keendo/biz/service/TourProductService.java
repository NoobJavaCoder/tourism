package com.keendo.biz.service;

import com.keendo.biz.mapper.TourProductMapper;
import com.keendo.biz.model.TourProduct;
import com.keendo.biz.service.bean.tourproduct.AddTourProduct;
import com.keendo.biz.service.bean.tourproduct.UpdateTourProduct;
import com.keendo.biz.service.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TourProductService {

    @Autowired
    private TourProductMapper tourProductMapper;

    @Autowired
    private UploadService uploadService;

    private final static String DIRECTORY_NAME = "/tourism/product/coverImg";//旅游产品封面图存放路径


    public List<TourProduct> getListByPage(Integer startIndex,Integer pageSize){
        List<TourProduct> tourProductList = tourProductMapper.selectByPage(startIndex, pageSize);
        return tourProductList;
    }

    public Integer count(){
        return tourProductMapper.count();
    }

    /**
     * 上传图片
     *
     * @param multipartFile
     * @return
     */
    public String uploadPic(MultipartFile multipartFile) {
        return uploadService.uploadPic(multipartFile, DIRECTORY_NAME, String.valueOf(System.currentTimeMillis()));
    }

    public TourProduct getById(Integer id){
        return tourProductMapper.selectById(id);
    }

    public Integer add(AddTourProduct addTourProduct){
        TourProduct tourProduct = BeanUtils.copyBean(addTourProduct, TourProduct.class);
        return this.save(tourProduct);
    }

    public Integer update(TourProduct tourProduct){
        return tourProductMapper.updateByPrimaryKey(tourProduct);
    }

    public Integer deleteById(Integer id){
        return tourProductMapper.deleteById(id);
    }

    private Integer save(TourProduct tourProduct){
        return tourProductMapper.insert(tourProduct);
    }

}
