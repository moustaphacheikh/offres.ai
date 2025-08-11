package com.mccmr.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GeneralLib$FichePaieReportInput {
   private JRBeanCollectionDataSource fichPaieDataSource;
   // $FF: synthetic field
   final GeneralLib this$0;

   public GeneralLib$FichePaieReportInput(final GeneralLib var1) {
      this.this$0 = this$0;
   }

   public JRBeanCollectionDataSource getFichPaieDataSource() {
      return this.fichPaieDataSource;
   }

   public void setFichPaieDataSource(JRBeanCollectionDataSource var1) {
      this.fichPaieDataSource = fichPaieDataSource;
   }

   public Map<String, Object> getParameters() {
      Map<String, Object> param = new HashMap();
      param.put("PARAMGEN_NOMENTREPRISE", this.this$0.menu.paramsGen.getNomEntreprise());
      param.put("PARAMGEN_ACTIVITEENTREPRISE", this.this$0.menu.paramsGen.getActiviteEntreprise());
      param.put("PARAMGEN_TELEPHONE", this.this$0.menu.paramsGen.getTelephone());
      param.put("PARAMGEN_FAX", this.this$0.menu.paramsGen.getFax());
      param.put("PARAMGEN_ADRESSE", this.this$0.menu.paramsGen.getAdresse());
      param.put("PARAMGEN_BD", this.this$0.menu.paramsGen.getBd());
      param.put("PARAMGEN_SITEWEB", this.this$0.menu.paramsGen.getSiteweb());
      param.put("PARAMGEN_EMAIL", this.this$0.menu.paramsGen.getEmail());
      param.put("PARAMGEN_NOCNAM", this.this$0.menu.paramsGen.getNoCnam());
      param.put("PARAMGEN_NOCNSS", this.this$0.menu.paramsGen.getNoCnss());
      param.put("PARAMGEN_NOITS", this.this$0.menu.paramsGen.getNoIts());
      param.put("PARAMGEN_VILLESIEGE", this.this$0.menu.paramsGen.getVilleSiege());
      param.put("PARAMGEN_PUB", this.this$0.menu.paramsGen.getPub());
      if (this.this$0.menu.paramsGen.getLogo() != null && this.this$0.menu.paramsGen.getLogo().length > 0) {
         byte[] bAvatar = this.this$0.menu.paramsGen.getLogo();
         InputStream targetStream = new ByteArrayInputStream(bAvatar);
         param.put("PARAMGEN_LOGO", targetStream);
      }

      return param;
   }

   public Map<String, Object> getDataSources() {
      Map<String, Object> dataSources = new HashMap();
      dataSources.put("fichPaieDataSource", this.fichPaieDataSource);
      return dataSources;
   }
}
