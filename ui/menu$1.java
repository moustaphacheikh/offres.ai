package com.mccmr.ui;

class menu$1 extends Thread {
   // $FF: synthetic field
   final menu this$0;

   menu$1(final menu var1) {
      this.this$0 = this$0;
   }

   public void run() {
      this.this$0.pc.refresh();
      this.this$0.showChartGen();
      this.this$0.progressBar.setValue(10);
      this.this$0.showChartAges();
      this.this$0.progressBar.setValue(20);
      this.this$0.showChartAnc();
      this.this$0.progressBar.setValue(40);
      this.this$0.showChartDep();
      this.this$0.progressBar.setValue(60);
      this.this$0.showChartEffByPoste();
      this.this$0.progressBar.setValue(70);
      this.this$0.showChartGenre();
      this.this$0.progressBar.setValue(80);
      this.this$0.showSalInfos();
      this.this$0.progressBar.setValue(100);
   }
}
